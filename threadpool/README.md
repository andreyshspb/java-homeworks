# Pool

* В задании нужно реализовать простой пул задач с фиксированным числом потоков (число задается в конструкторе)
* При создании объекта `ThreadPoolImpl` в нем должно начать работу `n` потоков
* У каждого потока есть два состояния: ожидание задачи / выполнение задачи
* Задача — вычисление некоторого значения, вызов `get` у объекта типа `Supplier<R>`
* При добавлении задачи, если в пуле есть ожидающий поток, то он должен приступить к ее исполнению. Иначе задача будет ожидать исполнения пока не освободится какой-нибудь поток
* Задачи, принятые к исполнению, представлены в виде объектов интерфейса `LightFuture`
* Метод `shutdown` должен завершить работу потоков. Для того, чтобы прервать работу потока, рекомендуется пользоваться методом `Thread.interrupt()`

---

# LightFuture

* Метод `isReady` возвращает `true`, если задача выполнена
* Метод `get` возвращает результат выполнения задачи
    * В случае, если соответствующий задаче `supplier` завершился с исключением, этот метод должен завершиться с исключением `LightExecutionException`
    * Если результат еще не вычислен, метод ожидает его и возвращает полученное значение

* Метод `thenApply` — принимает объект типа `Function`, который может быть применен к результату данной задачи `X` и возвращает новую задачу `Y`, принятую к исполнению
    * Новая задача будет исполнена не ранее, чем завершится исходная
    * В качестве аргумента объекту `Function` будет передан результат исходной задачи, и все `Y` должны исполняться на общих основаниях (т.е. должны разделяться между потоками пула)
    * Метод `thenApply` может быть вызван несколько раз

---

# Примечания

* В данной работе запрещено использование содержимого пакета `java.util.concurrent`, кроме классов `ReentrantLock` и `Condition` из этого пакета
* Все интерфейсные методы должны быть потокобезопасны
* Для каждого базового сценария использования должен быть написан несложный тест
    * Также должен быть написан тест проверяющий, что в тред-пуле действительно не менее `n` потоков

Мягкий дедлайн: 17.05.2021 23:59

Жёсткий дедлайн: 24.05.2021 23:59
