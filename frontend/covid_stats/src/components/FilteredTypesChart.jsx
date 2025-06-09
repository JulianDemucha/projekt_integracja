import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import axios from 'axios'
import {
    ResponsiveContainer,
    LineChart,
    Line,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
} from 'recharts'
import {DownloadButtonFiltered} from '../components/DownloadButtonFiltered.jsx'
import '../FilteredTypesChart.css'

export default function FilteredTypesChart() {
    const [chartData, setChartData] = useState([])
    const [types, setTypes] = useState([])
    const [yearRange, setYearRange] = useState([Infinity, -Infinity])
    const [selectedRange, setSelectedRange] = useState([0, 0])
    const [selectedTypes, setSelectedTypes] = useState({})

    const loggedIn = !!localStorage.getItem('user')

    useEffect(() => {
        axios
            .get('http://localhost:8080/api/stats3')
            .then((resp) => {
                const data = resp.data
                const allTypes = Array.from(new Set(data.map((d) => d.type)))
                setTypes(allTypes)

                const allYears = Array.from(new Set(data.map((d) => d.year))).sort(
                    (a, b) => a - b
                )
                const minYear = allYears[0]
                const maxYear = allYears[allYears.length - 1]
                setYearRange([minYear, maxYear])
                setSelectedRange([minYear, maxYear])

                const initSel = {}
                allTypes.forEach((t) => (initSel[t] = true))
                setSelectedTypes(initSel)

                const pivoted = allYears.map((yr) => {
                    const yearObj = { year: yr }
                    data
                        .filter((d) => d.year === yr)
                        .forEach((d) => {
                            yearObj[d.type] = d.count
                        })
                    allTypes.forEach((t) => {
                        if (yearObj[t] === undefined) yearObj[t] = 0
                    })
                    return yearObj
                })
                setChartData(pivoted)
            })
            .catch((err) => console.error('Error fetching data:', err))
    }, [])

    const handleTypeToggle = (typeKey) => {
        setSelectedTypes((prev) => ({
            ...prev,
            [typeKey]: !prev[typeKey],
        }))
    }

    const handleMinYearChange = (e) => {
        const newMin = Number(e.target.value), currMax = selectedRange[1]
        setSelectedRange([newMin > currMax ? currMax : newMin, currMax])
    }
    const handleMaxYearChange = (e) => {
        const newMax = Number(e.target.value), currMin = selectedRange[0]
        setSelectedRange([currMin, newMax < currMin ? currMin : newMax])
    }

    const [selMinYear, selMaxYear] = selectedRange
    const filteredData = chartData.filter(
        (d) => d.year >= selMinYear && d.year <= selMaxYear
    )

    // Build export URL with query params
    const queryParams = new URLSearchParams()
    queryParams.set('minYear', selMinYear)
    queryParams.set('maxYear', selMaxYear)
    Object.entries(selectedTypes).forEach(([type, isOn]) => {
        if (isOn) queryParams.append('types', type)
    })
    const exportUrl = `http://localhost:8080/api/stats3/export?${queryParams.toString()}`

    const COLORS = {
        restauracje: '#8884d8',
        bary: '#ff7300',
        'stołówki': '#ffc658',
        'punkty gastronomiczne': '#82ca9d',
    }

    return (
        <div className="filtered-chart-container">
            <div className="chart-wrapper">
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
                            contentStyle={{
                                borderRadius: 10,
                                background: 'rgba(50, 50, 50, 0.85)',
                            }}
                            labelStyle={{ color: 'grey' }}
                        />
                        <Legend
                            wrapperStyle={{
                                fontSize: '14px',
                                color: '#333',
                                marginBottom: -10,
                            }}
                            onClick={(e) => handleTypeToggle(e.dataKey)}
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
                                    isAnimationActive
                                    animationDuration={1200}
                                />
                            ) : null
                        )}
                    </LineChart>
                </ResponsiveContainer>
            </div>

            <div className="filters-container">
                <div
                    className="filters-panel"
                    style={{
                        filter: loggedIn ? 'none' : 'blur(3px)',
                        pointerEvents: loggedIn ? 'auto' : 'none',
                    }}
                >
                    <h3 className="filters-title">Filtry wykresu</h3>

                    <div className="filter-section">
                        <label className="filter-label">Zakres lat:</label>
                        <div className="filter-range-wrapper">
                            <div className="filter-range-row">
                                <span className="filter-range-text">Od:</span>
                                <input
                                    type="range"
                                    min={yearRange[0]}
                                    max={yearRange[1]}
                                    step={1}
                                    value={selMinYear}
                                    onChange={handleMinYearChange}
                                    className="filter-range-input"
                                />
                                <span className="filter-range-value">{selMinYear}</span>
                            </div>
                            <div className="filter-range-row">
                                <span className="filter-range-text">Do:</span>
                                <input
                                    type="range"
                                    min={yearRange[0]}
                                    max={yearRange[1]}
                                    step={1}
                                    value={selMaxYear}
                                    onChange={handleMaxYearChange}
                                    className="filter-range-input"
                                />
                                <span className="filter-range-value">{selMaxYear}</span>
                            </div>
                        </div>
                    </div>

                    <div className="filter-section">
                        <label className="filter-label">
                            Wybierz typ(y) obiektów gastronomicznych:
                        </label>
                        <div className="filter-checkboxes">
                            {types.map((t) => (
                                <label key={t} className="filter-checkbox-label">
                                    <input
                                        type="checkbox"
                                        checked={!!selectedTypes[t]}
                                        onChange={() => handleTypeToggle(t)}
                                    />
                                    {t}
                                </label>
                            ))}
                        </div>
                        <div style={{ marginBottom: -50, marginTop: 10 }}>
                            <DownloadButtonFiltered
                                baseUrl="http://localhost:8080/api/stats3/export"
                                params={{
                                    minYear: selMinYear,
                                    maxYear: selMaxYear,
                                    types: Object.keys(selectedTypes).filter(t => selectedTypes[t])
                                }}
                                filename="stats3_filtered.csv"
                                label="Eksportuj wyfiltrowane dane"
                            />
                        </div>
                    </div>
                </div>

                {!loggedIn && (
                    <div className="filters-overlay">
                        <p className="overlay-text">
                            Zaloguj się, aby uzyskać dostęp do filtrów
                        </p>
                        <Link to="/auth/login" className="overlay-button">
                            Przejdź do logowania
                        </Link>
                    </div>
                )}
            </div>


        </div>
    )
}
