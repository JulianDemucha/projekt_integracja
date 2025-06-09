package com.covid_stats.covid_stats.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

    //(x) ojapierdole jeszcze wiecej cwelowskich adnotacji

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(nullable = false, length = 500) // max 500 znakow
    @Getter @Setter
    private String content;

    @Column(nullable = false)
    @Getter @Setter
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY) /* (x) tu ciezsza sprawa troche z tym fetchtype, ale @manytoone wiadomo ze
    dziwka okresla relacje. (pole jest typu AppUser wiec relacja jest Comment -> AppUser (ManyToMany)
    no to ten fetchtype w skrocie robi ci tak ze jak masz komentarz i on ma pole tego autora to przy instancji
    tak jakby od razu nie laduje ci sie ten autor tylko taki kurwa strach na wroble co go imituje ale nie ma
    zadnych informacji o nim no i on sie zaladowuje dopiero jak sie do niego odwolujesz

        realnie jak masz z 5 komentarzy to pewnie nic nie zmienia ale jakby mi jakas sigma najebala z 200 to juz
        optymalizacja strony by byla bardziej sigmastyczna
     */
    @JoinColumn(name = "user_id", nullable = false) // (x) tym ustalasz klucz obcy (czyli id w appuser)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // żeby autor pola od proxy sie nie serializowaly
    /* (x) jak pozniej chcesz restem wyslac instancje jakas tego Commenta i masz to jebane proxy (przez ten fetchtype lazy)
    to dziwka se dodaje jakies pola "hibernateLazyInitializer", "handler" szczerze chuj mnie to czemu i po co aktualnie,
     ale tak jest no i ta adnotacja robi ze jak sie serializuje w jsona to nie bierze pod uwage tych pol cwelowskich
     */
    @Getter @Setter
    private AppUser author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference /* dzieki temu pomija to pole i sie nie zapetla serializacja
    (gdyby odpowiedzi mogly miec odpowiedzi, mimo ze tak nie ma na ten moment) */

    /* (x) oj bo kurwa front po fetchu i tak se widzi liste odpowiedzi ktora jest nizej.
    a to tak bardziej jest ze jakby inne odpowiedzi tez mogly miec odpowiedzi to zeby sie tak po kolei nie zapetlalo.
    a jak nie czaisz po co to pole w sumie jak jest lista odpowiedzi szczegolnie skoro front se radzi bez tego,
    to - glownie po to zeby elegancko dalo sie odwolywac do komentarzy nadrzednych, plus tak jest bardziej cleancodeowo
    i se mozna relacje w bazie pierdolnac i zamiast oddzielnej tabeli replies masz normalnie tylko pole kurwa parent
    i kazdy rekord commenta moze miec parenta

    a i jezeli parent == null to znaczy ze jest tym glownym i sigma
     */
    @Getter @Setter
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    /*(x)
    cascadetype all:
        to zeby operacje jakiekolwiek na komentarzu nadrzednym (czyli posiadajacego ta liste cn)
        aktualizowaly tez te zawarte w liscie. czyli np jak wypierdolisz ten nadrzedny do smietnika w pizdu to i te wszystkie
        wypierdala bo w sumie i tak nie sa potrzebne
    orphanremoval true:
        to w druga strone troche dziala bo ze jak masz jakis komentarz i odpowiedz do niego to jak wypierdolisz odpowiedz,
        to referencje w tej liscie w komentarzu nadrzednym do tej odpowiedzi wypierdala
        tl;dr czyli jak nie bedziesz mial tego orphanremoval to po usunieciu odpowiedzi w liscie w obiekcie komentarza dalej
        zostanie referencja
     */

    @JsonManagedReference // ma serializowac nie ma pomijac
    // (x) to odwrotnosc @JsonBackReference czyli tak jakby na wszelki wymusza serializacje
    @Getter @Setter
    private List<Comment> replies = new ArrayList<>();

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    public Comment(String content, AppUser author, Comment parent) {
        this.content = content;
        this.author = author;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }
}
    //(x) teraz wypierdalasz do AppUserRepo ty kurwo piekna
    //(x) -> ../Repositories/AppUserRepo