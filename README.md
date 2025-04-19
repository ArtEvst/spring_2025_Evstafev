# spring_2025_Evstafev

## Load Testing
Использовал [Fortio](https://github.com/fortio/fortio?tab=readme-ov-file)

Добавил одно значение в таблицу перед тестированием. Будем ломиться в него GET запросами. 

Settings: 
- GET
- URL: http://localhost:8080/api/v1/get?customerId=1
- QPS: 100'000
- Duration: 20s
- Threads/Simultaneous connections: \{4, 40, 4\}
- Pool size: 64

В таблице представлены результаты НТ: qps от Threads для 2-ух реализаций.

<img src="results/qps_from_connections.png" width="600"/>

Как видим наибольший qps при 96, но иметь более 32 нагружающих потоков нет большой необходимости, прирост незначительный.
qps для HashMap выше чем для PostgreSQL по ряду причин: 
1. Время доступа к данным в RAM меньше чем к буферному кэшу(и тем более к диску)
2. Накладные расходы на взаимодействие с базой данных(парсинг SQL, сетевое взаимодействие)
3. Внутренние процессы PostgreSQL(MVCC)

Протестируем зависимость qps от connection pool size для реализации с PostgreSQL.

Settings: 
- GET
- URL: http://localhost:8080/api/v1/get?customerId=1
- QPS: 100'000
- Duration: 20s
- Threads/Simultaneous connections: 32

В таблице представлены результаты НТ: qps от connection pool size для реализации с PostgreSQL.

| pool_size |     qps |
|-----------|---------|
|         4 |  20906.8|
|         8 |  25494.0|
|        12 |  25870.6|
|        16 |  28117.9|
|        20 |  28284.5|
|        24 |  28219.0|
|        28 |  28956.5|
|        32 |  29134.8|
|        36 |  29517.4|
|        40 |  29863.1|
|        44 |  27825.7|
|        48 |  30892.9|
|        52 |  30340.2|
|        56 |  30136.0|
|        60 |  30192.4|
|        64 |  31500.2|
|        68 |  30964.7|
|        72 |  30300.9|
|        76 |  28767.6|
|        80 |  29053.0|

Оптимальное значение connection pool size = 64. Данные об утилизации сохранены в файлах cpu_usage_*
