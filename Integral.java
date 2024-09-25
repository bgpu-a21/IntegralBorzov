package org.integral;

import java.util.function.Function;

public class Integral {
    private double a;
    private double b;
    private double h;
    private int threadCount;
    private Function<Double, Double> function;

    public Integral(double a, double b, double h, int threadCount, Function<Double, Double> function) {
        this.a = a;
        this.b = b;
        this.h = h;
        this.threadCount = threadCount;
        this.function = function;
    }

    public double calc() {
        double[] results = new double[threadCount];
        //Создаём массив для сохранения результата на каждой итерации
        Thread[] threads = new Thread[threadCount];
        //Создание массива потоков
        double step = (b - a) / threadCount;
        // Длина шага для каждого потока
        for (int i = 0; i < threadCount; i++) {
            int threadIndex = i;
            threads[i] = new Thread(() -> {
                double localSum = 0.0;
                double localA = a + threadIndex * step;
                double localB = a + (threadIndex + 1) * step;

                //Находим границы на каждой итерации

                // Интегрирование методом трапеций для текущего отрезка
                for (double x = localA + h; x < localB; x += h) {
                    localSum += function.apply(x);
                }

                localSum += (function.apply(localA) + function.apply(localB)) / 2.0;
                results[threadIndex] = localSum * h;
            });

            threads[i].start();//Запускаем поток
        }

        // Ждем завершения всех потоков
        for (int i = 0; i < threadCount; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Суммируем результаты
        double totalSum = 0.0;
        for (double result : results) {
            totalSum += result;
        }

        return totalSum;
    }
}

