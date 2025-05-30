// src/components/HomePage.jsx
import React from 'react';
import ECommerceChart from "./ECommerceChart.jsx";

export default function HomePage() {
    return (
        <div>
            <h2>Witaj na stronie głównej!</h2>
            {<ECommerceChart/>}
        </div>
    );
}
