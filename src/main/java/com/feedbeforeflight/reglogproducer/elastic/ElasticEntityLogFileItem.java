package com.feedbeforeflight.reglogproducer.elastic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.feedbeforeflight.onec.reglog.data.LogFileItem;
import lombok.Getter;
import lombok.Setter;

import org.springframework.context.annotation.PropertySource;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@JsonAutoDetect
@PropertySource("classpath:application.properties")
@Document(indexName = "#{@environment.getProperty('elastic-index-name')}")
public class ElasticEntityLogFileItem extends LogFileItem {

    @Getter @Setter
    @Id
    private String id; // format: [filename without extension].[row number]

    @JsonIgnore
    public void createId(String fileName, int rowNumber) {
        id = fileName + "." + rowNumber;
    }

    @JsonIgnore
    @Override
    public String toString() {
        return id + ", "+
                timestamp.toString() + ", " +
                (transactionState == null ? "" : transactionState.getName()) + ", " +
                (transactionDate == null ? "-" : transactionDate.toString()) + ", " +
                (transactionNumber == 0 ? "-" : transactionNumber) + ", " +
                username + ", " +
                computer + ", " +
                application + ", " +
                connection + ", " +
                event + ", " +
                (eventImportance == null ? "" : eventImportance.getName()) + ", " +
                comment + ", " +
                metadata + ", " +
                data + ", " +
                dataRepresentation + ", " +
                server + ", " +
                mainPort + ", " +
                auxiliaryPort + ", " +
                session;
    }

}

// https://infostart.ru/1c/articles/182061/
//        1) Дата и время в формате "yyyyMMddHHmmss", легко превращается в дату функцией Дата();
//        2) Статус транзакции – может принимать четыре значения "N" – "Отсутствует", "U" – "Зафиксирована", "R" – "Не завершена" и "C" – "Отменена";
//        3) Транзакция в формате записи из двух элементов преобразованных в шестнадцатеричное число – первый – число секунд с 01.01.0001 00:00:00 умноженное на 10000, второй – номер транзакции;
//        4) Пользователь – указывается номер в массиве пользователей;
//        5) Компьютер – указывается номер в массиве компьютеров;
//        6) Приложение – указывается номер в массиве приложений;
//        7) Соединение – номер соединения;
//        8) Событие – указывается номер в массиве событий;
//        9) Важность – может принимать четыре значения – "I" – "Информация", "E" – "Ошибки",
//        "W" – "Предупреждения" и "N" – "Примечания";
//        10) Комментарий – любой текст в кавычках;
//        11) Метаданные – указывается номер в массиве метаданных;
//        12) Данные – самый хитрый элемент, содержащий вложенную запись;
//        13) Представление данных – текст в кавычках;
//        14) Сервер – указывается номер в массиве серверов;
//        15) Основной порт – указывается номер в массиве основных портов;
//        16) Вспомогательный порт – указывается номер в массиве вспомогательных портов;
//        17) Сеанс – номер сеанса;
//        18) Количество дополнительных метаданных, номера которых будут перечислены в следующих элементах записи. Именно 18-й элемент определяет длину записи, т.к. дальше будут следовать столько элементов сколько указано здесь + один последний, назначение которого пока не определено и обычно там "{0}". Возможно это просто маркер окончания записи. Так же есть идея что {0} похоже на пустой массив.
