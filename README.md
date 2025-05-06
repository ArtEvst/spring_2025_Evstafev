# spring_2025_Evstafev

## Load Testing
Использовал [Fortio](https://github.com/fortio/fortio?tab=readme-ov-file)

Задание заключалось в шардировании данных по индексу. В два эксземпляра PostgreSQL я добавил данных: четных в одну, нечетных в другую. К сожалению, Фортио не позволяет
отправлять запросы на два разных хоста одновременно, что плохо ведь get запрос возвращает попкупателя в одной из баз в зависимости от четности его id. Самое простое решения, которое я придумал, было запустить два контейнера с Фортио "одновременно". 
```
docker run --network host fortio/fortio load -quiet -qps 50000 -c 96 -t 40s "http://localhost:8080/api/v1/get?customerId=1" &
docker run --network host fortio/fortio load -quiet -qps 50000 -c 96 -t 40s "http://localhost:8080/api/v1/get?customerId=2"
```
Connections 96.

При pool_size = 32

Первый инстанс: 14589

Второй инстанс: 14559

Работали они "независимо" и мешали друг другу, наверно это и повлияло на уменьшение суммарного QPS

При pool_size = 32 стало чуть лучше, суммарный QPS: 30033



Результаты:

Даже 

Попробуем меньше коннектов.
```
docker run --network host fortio/fortio load -quiet -qps 50000 -c 32 -t 40s "http://localhost:8080/api/v1/get?customerId=1" &
docker run --network host fortio/fortio load -quiet -qps 50000 -c 32 -t 40s "http://localhost:8080/api/v1/get?customerId=2"
```
Connections 32.

| Pool size | QPS    | p50     | p90     | p99     |
|-----------|--------|---------|---------|---------|
|      64   | 33118  | 2.89 ms | 3.83 ms | 4.90 ms |
|      32   | 32057  | 2.77 ms | 3.92 ms | 8.93 ms|
