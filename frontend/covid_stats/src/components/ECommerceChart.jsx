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

    return (
        <div style={{ width: '100%', height: 400 }}>
            <ResponsiveContainer>
                <BarChart data={data} margin={{ top: 40, right: 60, left: 40, bottom: 40 }}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="year" tickFormatter={(tick) => String(tick)} />
                    <YAxis unit="%" />
                    <Tooltip formatter={(value) => `${value}%`} />
                    <Legend />
                    <Bar dataKey="percent" name="Procent przedsiębiorstw" fill="#8884d8" />
                </BarChart>
            </ResponsiveContainer>
        </div>
    );
}
