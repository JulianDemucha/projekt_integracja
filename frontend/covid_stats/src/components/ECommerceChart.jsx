import React, { useState, useEffect } from 'react';
import {
    ResponsiveContainer,
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
    Cell,
} from 'recharts';
import axios from 'axios';

export default function ECommerceChart() {
    const [data, setData] = useState([]);

    useEffect(() => {
        axios
            .get('http://localhost:8080/api/stats')
            .then((response) => setData(response.data))
            .catch((err) => console.error('Error fetching data:', err));
    }, []);


    const customLegendPayload = [
        { value: '2020–2021 (pandemia)', type: 'circle', color: '#ff7f0e', id: '2020-2021' },
        { value: 'Inne lata',  type: 'circle', color: '#8884d8', id: 'inne' },
    ];

    return (
        <div style={{ width: 700, height: 400, margin: 'auto' }}>
            <ResponsiveContainer>
                <BarChart data={data} margin={{ top: 20, right: 30, left: 20, bottom: 20 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="year" tickFormatter={(tick) => String(tick)} />
                    <YAxis unit="%" />
                    <Tooltip
                        itemStyle={{color: 'rgba(200, 200, 200, 0.9)'}}
                        contentStyle={{ borderRadius: 10, background: 'rgba(50, 50, 50, 0.85)' /* 85% transparent */}}
                        labelStyle={{ color: 'rgba(200, 200, 200, 1)'}}
                    formatter={(value) => `${value}%`} />

                    <Legend
                        wrapperStyle={{ color: '#333333', fontSize: '14px' }}
                        payload={customLegendPayload}
                        formatter={(value, entry) => (
                            <span style={{ color: entry.color, fontWeight: 'bold' }}>
                                {value}
                            </span>
                        )}
                    />

                    <Bar
                        dataKey="percent"
                        name="Procent przedsiębiorstw posiadających opcję zakupów online"
                        barSize={40}
                        radius={[5, 5, 0, 0]}
                        isAnimationActive={true}
                        animationBegin={0}
                        animationDuration={1500}
                        animationEasing="ease"
                    >
                        {data.map((entry, index) => (
                            <Cell
                                key={`cell-${index}`}
                                fill={
                                    entry.year === 2020 || entry.year === 2021
                                        ? '#ff7f0e'
                                        : '#8884d8'
                                }
                            />
                        ))}
                    </Bar>
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
}
