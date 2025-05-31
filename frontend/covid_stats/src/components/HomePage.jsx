import React from 'react';
import ECommerceChart from "./ECommerceChart.jsx";
import FilteredTypesChart from "./FilteredTypesCharts.jsx";

export default function HomePage() {
    return (
        <div>
            <h1>Wpływ pandemii COVID-19 na e-commerce w europie</h1>
            <br/><br/><br/>
            <h3>Wykres przedstawia % przedsiębiorstw z opcją zakupów online w latach 2013-2023</h3>
            {<ECommerceChart/>}
            {<FilteredTypesChart/>}

        </div>
    );
}
