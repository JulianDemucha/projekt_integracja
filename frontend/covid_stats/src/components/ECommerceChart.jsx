import React, {useState, useEffect} from 'react';
import {
    ResponsiveContainer, BarChart, Bar, XAxis, YAxis,
    CartesianGrid, Tooltip, Legend, Cell
} from 'recharts';
import axios from 'axios';

export default function ECommerceChart() {
    const [data, setData] = useState([]);

    useEffect(() => {
        axios
            .get('http://localhost:8080/api/stats')
            .then((res) => setData(res.data))
            .catch((err) => console.error('Error fetching data:', err));
    }, []);

    const legendPayload = [
        {value: '2020–2021 (pandemia)', type: 'circle', color: '#ff7f0e', id: 'pandemia'},
        {value: 'Inne lata', type: 'circle', color: '#8884d8', id: 'inne'},
    ];

    return (
        <div style={{width: 700, margin: 'auto'}}>

            <div style={{height: 400}}>
                <ResponsiveContainer>
                    <BarChart data={data} margin={{top: 20, right: 30, left: 20, bottom: 20}}>
                        <CartesianGrid strokeDasharray="3 3"/>
                        <XAxis dataKey="year" tickFormatter={(t) => String(t)}/>
                        <YAxis unit="%"/>
                        <Tooltip formatter={(v) => `${v}%`}
                                 itemStyle={{color: 'rgba(200,200,200,0.9)'}}
                                 contentStyle={{borderRadius: 10, background: 'rgba(50,50,50,0.85)'}}
                                 labelStyle={{color: 'rgba(200,200,200,1)'}}/>
                        <Legend payload={legendPayload}
                                wrapperStyle={{color: '#333', fontSize: '14px'}}
                                formatter={(v, entry) => <span
                                    style={{color: entry.color, fontWeight: 'bold'}}>{v}</span>}/>
                        <Bar dataKey="percent" name="% firm z e-commerce" barSize={40} radius={[5, 5, 0, 0]}>
                            {data.map((e, i) => (
                                <Cell key={`c-${i}`}
                                      fill={(e.year === 2020 || e.year === 2021) ? '#ff7f0e' : '#8884d8'}/>
                            ))}
                        </Bar>
                    </BarChart>
                </ResponsiveContainer>
            </div>



        </div>
    );
}
