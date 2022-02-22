# Kolokwium - grupa A

## Zadanie 1 - MonteCarlo

Celem jest implementacja całkowanie metodą MonteCarlo realizowanego za pomocą wątków.

Całkowanie metodą MC jest realizowane według prostego algorytmu

```
Dana jest funkcja podcałkowa f, przedział [a,b] oraz duża liczba całkowita n
sum=0
for i in [1,n]:
	Wylosuj x z przedziału [a,b]
	Oblicz sum += f(x) * (b-a) / n
Zwróć sum
```

1. Klasa `MonteCarlo`
   - Zadeklaruj klasę `MonteCarlo` z metodą `main()`
   - Wewnątrz zadeklaruj klasę wątku `MCThread`
   - Wątek ma mieć 4 atrybuty przekazane w konstruktorze:
     - `Function<Double,Double> f`
     - `double a – dolna granica`
     - `double b – górna granica`
     - `int n – liczba iteracji`
   - Napisz metodę `integrateSequential(int n)`, w której tworzony jest wątek i wywoływana jego metoda `run()` wątku. Przekaż funkcję podcałkową jako wyrażenia lambda. Na przykład może to być `sin(x)` w przedziale [0,`Math.PI`]
2. `integrateParallel(int k, int n)`
   - Utwórz `k` wątków
   - Dodaj do klasy `MonteCarlo` kolejkę
   - Obliczona całka będzie średnią z wartości zwróconej przez wątki
3. dobór `k` i `n`
   - Wyznacz czas obliczeń
   - Wybierz bardziej złożoną funkcję podcałkową (np. funkcję gęstości rozkładu Gaussa o wybranej wartości średniej i wariancji)
   - Dobierz parametry `k` i `n` tak, aby otrzymać krótkim czasie w miarę dokładny wyniki uruchamiając `integrateParallel()` w pętli

## Zadanie 2 - Przystanki
W pliku [`przystanki.csv`](examples/przystanki.csv) zebrano informacje o przystankach komunikacji miejskiej w Tarnobrzegu. Zadeklaruj klasę Przystanek, która posłuży do przechowywania danych o przystanku z polami odpowiadającymi kolumnom tabeli i wczytaj te dane do pamięci.

1. Wypisz wszystkie przystanki przy drogach, których symbol ma pierwszą literę P
2. Wypisz wszystkie przystanki mieszczące się wewnątrz obszaru `(50.54 21.63) - (50.62 21.73)` posortowane według nazwy
3. Wypisz wszystkie przystanki przy ulicy Warszawskiej posortowane według kilometrażu. Uwaga 2+210 oznacza 2.210 km od ustalonego początku drogi. Patrz [Pikietaż](https://pl.wikipedia.org/wiki/Pikieta%C5%BC)
4. Wyznacz współrzędne prostokąta zawierającego wszystkie przystanki