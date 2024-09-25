package org.integral;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

class Result {
    int threadCount;
    double result;
    long time;

    public Result(int threadCount, double result, long time) {
        this.threadCount = threadCount;
        this.result = result;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Число потоков: " + threadCount + " Результат вычислений: " + result + " Время вычисления: " + time + " ns";
    }
}

public class Main {
    public static void main(String[] args) {

        double a = 0;
        double b = Math.PI;
        double h = Math.pow(10, -6); // Точность вычислений
        int treadMax = 20;
        Function<Double, Double> function = Math::sin; // Задаём функцию для интеграла
        List<Result> results = new ArrayList<>(); // Список для хранения результатов

        for (int thead = 1; thead < treadMax + 1; thead++) {
            Integral integral = new Integral(a, b, h, thead, function); // Вызываем класс для вычисления интеграла
            long startTime = System.nanoTime(); // Начальное время
            double result = integral.calc(); // Находим интеграл
            long endTime = System.nanoTime(); // Конечное время
            long duration = endTime - startTime;

            // Добавляем результат в список
            results.add(new Result(thead, result, duration));
        }

        // Сортируем результаты по времени выполнения
        results.sort(Comparator.comparingLong(r -> r.time));

        // Выводим отсортированные результаты
        for (Result r : results) {
            System.out.println(r);
        }
        Result fastest = results.get(0); // Самый быстрый поток
        Result slowest = results.get(results.size() - 1); // Самый медленный поток

        // Выводим результаты
        System.out.println("Самый быстрый поток:");
        System.out.println(fastest);

        System.out.println("Самый медленный поток:");
        System.out.println(slowest);
    }
}
