package com.covid_stats.covid_stats.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Setter
    @Getter
    @Column(unique = true, nullable = false)
    private String username;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    @Setter
    private String role = "ROLE_USER";

    /*
     analizuje se caly projekt to dodam ci komentarze tu instrukcja kursu julasowego:
        - dam przed useless overall komentarzami (x) takie gowno co znaczy ze jak ogarniesz dany plik to wyjeb takie komentarze
        - ten tez wyjeb jak ogarniesz
        - a jak bedzie ci sie pierdolic git czy cos to chuj ja juz pousuwam
        - zanim pushniesz projekt po usunieciu komentarzy upewnij sie ze nic nie rozpierdoliles przypadkiem bozym
        - zeby pushnac robisz takie rachu ciachu:
            - git add . (tym dodajesz se do zapisu pod commita zmienione pliki, "." znaczy ze wszystkie zmienione wpierdalasz)
            - git commit -m "removed some comments" (commitujesz czyli wprowadzasz zmiany ale lokalnie, -m znaczy ze chesz
            dodac wiadomosc do commita (jak jej nie dodasz to bash dostaje pierdolca i wrzuca cie w jakas minigre undertale
            w ktorej musisz mashowac wszystkie przyciski w klawiaturze zevby z niej wyjsc)
            - git push (to pushujesz i sigma jest normalnbie w chmurze)
            - a i upewnij sie ze jestes na branchu dev nie main

     to tera tak krotko zeby wszystko wygladalo mniej confusing:
        - @Getter i @Setter to z lomboka (taka biblioteka ze w tle ci robi getter czy setter do konkretnego pola w klasie)
        - @Entity, @Id są z Hibernate czyli takiego frameworka ktory zajebiscie ulatwia robienie czegokolwiek z baza
        miedzy innymi mozesz se pyk pyk uzywac jakichs metod typu findByUsername(username) zamiast kurwa w sqlu jak pedal
        pisac select kurwa where username username cwel chuj cn ale to lepiej zobaczysz pozniej bo tu narazie jest tylko
        deklaracja encji
            -* polaczenie z baza jest zadeklarowane w ../resources/application.properites jak co
            - @Entity mowi hibernateowi ze to klasa ktora ma byc normalnie w bazie i elo
            - @Id ci ustawia pole jako klucz glowny
            - @GeneratedValue(strategy = GenerationType.IDENTITY) to ze dziwka baza ma sama generowac id
            (wydaje mi sie ze ta adnotacja w ogole jest tylko do pol z @Id), a to blablabla.identity to
            jak AUTO_INCREMENT czyli domyslnie tak jak normalnie robisz w bazie a nie poierdolenie sie w jakies
            sekwencje dla gejow
            - @Column jebana dziwka brzmi tak jakby deklarowala ze pole ma byc mapowane na baze nie?
            a chuja i tak sie pole zmapuje i tak jak masz @Entity kurwa jego mac
            dajesz se to jak chcesz dac jakies wlasciwosci do kolumnty tylko zeby dac w () cos cn
            no jak tam masz to nullable czy inny chuj to wiadomitka

     tyle w sumie jak ogaritka to wypierdalaj do Comment.java ( czyli Comment )
     a jak nie ogaritka to spierdalaj

     -> Comment

     */


}

