// src/components/FilteredTypesChart.jsx

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import {
    ResponsiveContainer,
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
} from 'recharts';

export default function FilteredTypesChart() {
    const [chartData, setChartData] = useState([]);
    const [types, setTypes] = useState([]);
    const [yearRange, setYearRange] = useState([Infinity, -Infinity]); // [minYear, maxYear]
    const [selectedRange, setSelectedRange] = useState([0, 0]);         // [selMinYear, selMaxYear]
    const [selectedTypes, setSelectedTypes] = useState({});            // { restauracje: true, … }

    useEffect(() => {
        axios
            .get('http://localhost:8080/api/stats3')
            .then((resp) => {
                const data = resp.data;

                const allTypes = Array.from(new Set(data.map((d) => d.type)));
                setTypes(allTypes);

                const allYears = Array.from(new Set(data.map((d) => d.year))).sort((a, b) => a - b);
                const minYear = allYears[0];
                const maxYear = allYears[allYears.length - 1];
                setYearRange([minYear, maxYear]);
                setSelectedRange([minYear, maxYear]);

                const initSel = {};
                allTypes.forEach((t) => {
                    initSel[t] = true;
                });
                setSelectedTypes(initSel);

                const pivoted = allYears.map((yr) => {
                    const yearObj = { year: yr };
                    data
                        .filter((d) => d.year === yr)
                        .forEach((d) => {
                            yearObj[d.type] = d.count;
                        });
                    allTypes.forEach((t) => {
                        if (yearObj[t] === undefined) {
                            yearObj[t] = 0;
                        }
                    });
                    return yearObj;
                });

                setChartData(pivoted);
            })
            .catch((err) => console.error('Error fetching data:', err));
    }, []);

    // Przełączanie widoczności typów
    const handleTypeToggle = (typeKey) => {
        setSelectedTypes((prev) => ({
            ...prev,
            [typeKey]: !prev[typeKey],
        }));
    };

    // Suwak: zmiana minimalnego roku
    const handleMinYearChange = (e) => {
        const newMin = Number(e.target.value);
        const currMax = selectedRange[1];

        if (newMin > currMax) {
            setSelectedRange([currMax, currMax]);
        } else {
            setSelectedRange([newMin, currMax]);
        }
    };

    // Suwak: zmiana maksymalnego roku
    const handleMaxYearChange = (e) => {
        const newMax = Number(e.target.value);
        const currMin = selectedRange[0];

        if (newMax < currMin) {
            setSelectedRange([currMin, currMin]);
        } else {
            setSelectedRange([currMin, newMax]);
        }
    };

    // Filtrowanie danych po zaznaczonym zakresie
    const [selMinYear, selMaxYear] = selectedRange;
    const filteredData = chartData.filter(
        (d) => d.year >= selMinYear && d.year <= selMaxYear
    );

    // Kolory dla typów
    const COLORS = {
        restauracje: '#8884d8',
        bary: '#82ca9d',
        'stołówki': '#ffc658',
        'punkty gastronomiczne': '#ff7300',
    };

    return (
        <div style={{ maxWidth: 1000, margin: '0 auto', padding: '1rem' }}>
            {/* Kontener flex: wykres po lewej, filtr po prawej */}
            <div style={{ paddingLeft: 150, display: 'flex', gap: '1rem' }}>
                {/* ─── Sekcja wykresu ─── */}
                <div style={{ flex: 1, height: 450, minWidth: 170, maxWidth: 1000  }}>
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart
                            data={filteredData}
                            margin={{ top: 20, right: 20, left: 20, bottom: 20 }}
                        >
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis
                                dataKey="year"
                                type="category"
                                interval={0}
                                tick={{ fontSize: 11, angle: -45, dy: 10 }}
                                tickFormatter={(tick) => String(tick)}
                            />
                            <YAxis />
                            <Tooltip
                                contentStyle={{borderRadius: 10, background: 'rgba(50, 50, 50, 0.85)' /* 85% transparent */}}
                                     labelStyle={{ color: 'grey'}}
                            />

                            <Legend
                                wrapperStyle={{ fontSize: '14px', color: '#333', marginBottom: -10 }}
                                onClick={(e) => {
                                    handleTypeToggle(e.dataKey);
                                }}
                            />

                            {types.map((t) =>
                                selectedTypes[t] ? (
                                    <Line
                                        key={t}
                                        type="monotone"
                                        dataKey={t}
                                        name={t}
                                        stroke={COLORS[t] || '#000000'}
                                        strokeWidth={2}
                                        dot={{ r: 3 }}
                                        activeDot={{ r: 6 }}
                                        isAnimationActive={true}
                                        animationDuration={1200}
                                    />
                                ) : null
                            )}
                        </LineChart>
                    </ResponsiveContainer>
                </div>

                {/* ─── Sekcja filtrów ─── */}

                <div
                    style={{
                        fontSize: '14px',
                        padding: 15,
                        paddingTop: 50,
                        paddingBottom: 80,
                        width: 200,                // szerokość panelu filtrów
                        border: '1px solid #ddd',
                        borderRadius: 8,
                        alignSelf: 'flex-start',   // tak, by filtr "wyrównywał się" do góry
                    }}
                >
                    <h3 style={{ marginBottom: '0.5rem' }}>Filtry wykresu</h3>

                    {/* Zakres lat */}
                    <div style={{ marginBottom: '1rem' }}>
                        <label style={{ display: 'block', marginBottom: '0.25rem' }}>
                            Zakres lat:
                        </label>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                            {/* pierwszy wiersz: „Od” + suwak */}
                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                <span>Od:</span>
                                <input
                                    type="range"
                                    min={yearRange[0]}
                                    max={yearRange[1]}
                                    step={1}
                                    value={selMinYear}
                                    onChange={handleMinYearChange}
                                    style={{ width: 150 }}
                                />
                                <span>{selMinYear}</span>
                            </div>

                            {/* drugi wiersz: „Do” + suwak */}
                            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                                <span>Do:</span>
                                <input
                                    type="range"
                                    min={yearRange[0]}
                                    max={yearRange[1]}
                                    step={1}
                                    value={selMaxYear}
                                    onChange={handleMaxYearChange}
                                    style={{ width: 150 }}
                                />
                                <span>{selMaxYear}</span>
                            </div>
                        </div>
                    </div>

                    {/* Checklist typów */}
                    <div>
                        <label style={{ display: 'block', marginBottom: '0.25rem' }}>
                            Wybierz typ(y) obiektów gastronomicznych:
                        </label>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
                            {types.map((t) => (
                                <label key={t} style={{ cursor: 'pointer' }}>
                                    <input
                                        type="checkbox"
                                        checked={!!selectedTypes[t]}
                                        onChange={() => handleTypeToggle(t)}
                                        style={{ marginRight: '0.25rem' }}
                                    />
                                    {t}
                                </label>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}
