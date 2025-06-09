import React, {useState, useEffect} from 'react';
import {
    ResponsiveContainer, LineChart, Line, XAxis, YAxis,
    CartesianGrid, Tooltip, Legend
} from 'recharts';
import axios from 'axios';

export default function GastronomyRevenueChart() {
    const [data, setData] = useState([]);

    useEffect(() => {
        axios
            .get('http://localhost:8080/api/stats2')
            .then((res) => setData(res.data))
            .catch((err) => console.error('Error fetching data:', err));
    }, []);

    return (
        <div style={{width: 700, margin: 'auto'}}>

            <div style={{height: 400}}>
                <ResponsiveContainer width="100%" height="100%">
                    <LineChart
                        data={data}
                        margin={{top: 20, right: 20, left: 20, bottom: 40}}
                    >
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="year" tickFormatter={(t) => String(t)}/>
                        <YAxis tickFormatter={(t) => `${(t / 1e6).toFixed(1)} mln`}/>
                        <Tooltip
                            formatter={(v) => `${(v / 1e6).toFixed(2)} mln zł`}
                            labelFormatter={(l) => `Rok: ${l}`}
                            contentStyle={{borderRadius: 10, background: 'rgba(50,50,50,0.85)'}}
                            labelStyle={{color: 'lightgrey'}}
                        />
                        <Legend layout="horizontal" verticalAlign="bottom" align="center"
                                wrapperStyle={{fontSize: '14px'}}/>
                        <Line
                            type="monotone"
                            dataKey="revenue"
                            name="Przychody gastronomii"
                            stroke="#8884d8"
                            strokeWidth={5}
                            dot={{r: 3}}
                            activeDot={{r: 6}}
                            isAnimationActive
                            animationDuration={1200}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>



        </div>
    );
}
