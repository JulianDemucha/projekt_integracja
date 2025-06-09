package com.covid_stats.covid_stats.Repositories;
import com.covid_stats.covid_stats.Models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}

/*  (x)
    japidi no dobra to tu masz elegancko repozytorium czyli takie polaczenie klasy w kodzie z tabela w bazie
    no i tu sie zaczyna skurwysynstwo springowe troche bo pozniej mozesz miec lekka nieogaritke
    chodzi o @bean i @component ktore mozesz ogarnac w jakims filmiku jak dokladnie dzialaja, ale cosik
    sie postaram wytlumaczyc. tutaj nie uzywasz zadnych adnotacji, ale w tle tworzy ci sie taki bean -
    czyli instancja tej klasy (no bo to tylko klasa cn) i pozniej tam gdzie uzyjesz @autowired, to ci przydziela
    dokladnie te sama instancje klasy do pola ktore se tam stworzysz i dzieki temu masz jedno repozytorium
    na caly jebany projekt. ogolnie to czasem nie trzeba dawac tego autowired np w tym przypadku jak to jest
    repozytorium, bo to jest jakby unikalne i spring i tak wie ze ma to wstrzyknac blablabla nie musisz zczaic
    tego w sekunde

    dobra do meritum bo to do pozniejszego glownie bylo ale chcialem popierdolic na jakiej zasadzie to pozniej dziala
    ogolnie to wlasnie na repo uzywasz tych wszystkich metod zeby wykonywac operacje na bazach
    czyli jak aktualizujesz/usuwasz jakies pola i wgl

    dobra tera kierunek CommentRepository

    -> CommentRepository
 */