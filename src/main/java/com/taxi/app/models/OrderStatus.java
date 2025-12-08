package com.taxi.app.models;

public enum OrderStatus {
    NEW,            // создан клиентом, ожидает водителя
    ACCEPTED,       // принят конкретным водителем
    IN_PROGRESS,    // водитель нажал "приступить к работе"
    COMPLETED,      // работа завершена
    CANCELLED       // отменён
}