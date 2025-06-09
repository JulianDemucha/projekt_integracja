import React, {useContext} from 'react';
import ECommerceChart from '../components/ECommerceChart.jsx';
import FilteredTypesChart from '../components/FilteredTypesChart.jsx';
import GastronomyRevenueChart from '../components/GastronomyRevenueChart.jsx';
import CommentsList from '../components/CommentsList.jsx';
import {AuthContext} from '../context/AuthContext';
import '../comments.css';
import {DownloadButton} from "../components/DownloadButton.jsx";


export default function HomePage() {
    const {user} = useContext(AuthContext);
    return (
        <div>
            <h1>Wpływ pandemii COVID-19 na gastronomię w Europie</h1>
            <br/>
            <h3 style={{color: 'rgba(170, 170, 170, 1)'}}>
                Po wybuchu pandemii związanej z COVID-19 zostaliśmy zamknięci w domach
                bez możliwości odwiedzenia naszych ulubionych barów i restauracji.
                Wprowadzono surowe obostrzenia – lokale musiały zamknąć się już w marcu
                2020 roku, a w kolejnych miesiącach obowiązywały zakazy działalności
                stacjonarnej lub ograniczenia do sprzedaży na wynos. W efekcie wiele
                przedsiębiorstw gastronomicznych borykało się z nagłym spadkiem obrotów,
                wzrostem kosztów utrzymania lokalu przy jednoczesnym braku choćby
                częściowych przychodów.
            </h3>
            <br/>
            <h3
                style={{
                    color: 'rgba(170, 170, 170, 1)',
                    paddingLeft: 100,
                    paddingRight: 100,
                }}
            >
                Poniżej znajduje się wykres przedstawiający liczbę obiektów
                gastronomicznych w Polsce w latach 2005–2023, z podziałem na
                restauracje, bary, stołówki oraz punkty gastronomiczne. Najbardziej
                widoczny jest oczywisty i gwałtowny spadek w 2020 roku. Wykres oferuje
                także filtrowanie po latach i typach lokali.
            </h3>

            <FilteredTypesChart/>

            <h3
                style={{
                    color: 'rgba(170, 170, 170, 1)',
                    paddingLeft: 100,
                    paddingRight: 100,
                }}
            >
                Najbardziej dotknięte zostały restauracje i bary – nawet w 2023 roku
                liczba restauracji nadal nie wróciła do poziomu sprzed 2019 roku, a
                liczba barów dopiero nieznacznie ją przekroczyła. Wiele lokali, próbując
                przetrwać, skróciło godziny pracy lub przeszło na model „kuchni tylko na
                dowóz” – często jako jedyną realną formę kontynuowania działalności w
                nowych warunkach.
            </h3>
            <h3
                style={{
                    color: 'rgba(170, 170, 170, 1)',
                    paddingLeft: 100,
                    paddingRight: 100,
                }}
            >
                Ten zwrot w stronę dostaw wpisywał się w szerszy trend – rosnącego
                znaczenia sprzedaży online. Choć pandemia wyraźnie przyspieszyła
                potrzebę cyfryzacji, dane z kolejnego wykresu pokazują, że adaptacja
                firm do e-commerce przebiegała raczej stopniowo niż skokowo. Może to
                świadczyć o tym, że wiele przedsiębiorstw – zwłaszcza tych z bardziej
                tradycyjnych branż – natrafiało na liczne bariery technologiczne i
                organizacyjne, które spowalniały ten proces.
            </h3>

            <ECommerceChart/>
            <DownloadButton
                url="http://localhost:8080/api/stats/export"
                filename="stats.csv"
                label="Pobierz % e-commerce (CSV)"
            />

            <h3>
                Aby uzupełnić obraz sytuacji, warto przyjrzeć się również temu, jak
                zmieniały się całkowite przychody gastronomii w Polsce w badanym
                okresie. Poniższy wykres prezentuje sumę rocznych przychodów (w
                złotych) dla sektora gastronomicznego od końca lat 90. aż do 2023 roku.
                Dzięki temu widzimy, jak pandemia wpłynęła nie tylko na liczbę lokali,
                ale także na ich kondycję finansową.
            </h3>

            <GastronomyRevenueChart/>
            <DownloadButton
                url="http://localhost:8080/api/stats2/export"
                filename="stats2.csv"
                label="Pobierz przychody gastronomii (CSV)"
            />


            <div className="max-w-2xl mx-auto">
                <CommentsList user={user}/>
            </div>

        </div>
    );
}
