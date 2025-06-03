import React from 'react';

const Footer = () => {
    return (
        <footer className="bg-[#1e1e1e] text-gray-200 py-6 mt-10">
            <div className="container mx-auto px-4 text-center">
                <p className="text-sm mb-2">© {new Date().getFullYear()} Julas&amp;Hubercica</p>
                <p className="text-sm">Telefon: +48 600 300 200</p>
                <p className="text-sm">Adres: Ulica Pokątna 12, Londyn, Wielka Brytania</p>
            </div>
        </footer>
    );
};

export default Footer;
