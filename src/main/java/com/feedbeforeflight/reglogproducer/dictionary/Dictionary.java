package com.feedbeforeflight.reglogproducer.dictionary;

import com.feedbeforeflight.reglogproducer.batch.DictionaryItem;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Dictionary {

    private final HashMap<Integer, DictionaryUser> userMap;
    private final HashMap<Integer, DictionaryComputer> computerMap;
    private final HashMap<Integer, DictionaryApplication> applicationMap;
    private final HashMap<Integer, DictionaryEvent> eventMap;
    private final HashMap<Integer, DictionaryMetadata> metadataMap;
    private final HashMap<Integer, DictionaryServer> serverMap;
    private final HashMap<Integer, DictionaryMainPort> mainPortMap;
    private final HashMap<Integer, DictionaryAuxiliaryPort> auxiliaryPortMap;

    public Dictionary() {
        userMap = new HashMap<>();
        computerMap = new HashMap<>();
        applicationMap = new HashMap<>();
        eventMap = new HashMap<>();
        metadataMap = new HashMap<>();
        serverMap = new HashMap<>();
        mainPortMap = new HashMap<>();
        auxiliaryPortMap = new HashMap<>();
    }

    public String summary() {
        return new StringBuilder()
                .append("Users: ").append(userMap.size()).append(System.lineSeparator())
                .append("Computers: ").append(computerMap.size()).append(System.lineSeparator())
                .append("Applications: ").append(applicationMap.size()).append(System.lineSeparator())
                .append("Events: ").append(eventMap.size()).append(System.lineSeparator())
                .append("Metadata items: ").append(metadataMap.size()).append(System.lineSeparator())
                .append("Servers: ").append(serverMap.size()).append(System.lineSeparator())
                .append("Main ports: ").append(mainPortMap.size()).append(System.lineSeparator())
                .append("Auxiliary ports: ").append(auxiliaryPortMap.size()).append(System.lineSeparator())
                .toString();
    }

    public void addItem(DictionaryItem dictionaryItem) {
        switch (dictionaryItem.getTypeId()) {
            case 1:
                addUser(dictionaryItem.getFields());
                break;
            case 2:
                addComputer(dictionaryItem.getFields());
                break;
            case 3:
                addApplication(dictionaryItem.getFields());
                break;
            case 4:
                addEvent(dictionaryItem.getFields());
                break;
            case 5:
                addMetadata(dictionaryItem.getFields());
                break;
            case 6:
                addServer(dictionaryItem.getFields());
                break;
            case 7:
                addMainPort(dictionaryItem.getFields());
                break;
            case 8:
                addAuxiliaryPort(dictionaryItem.getFields());
                break;
        }
    }

    private void addUser(String[] fields) {
        DictionaryUser user = new DictionaryUser(Integer.valueOf(fields[2]), fields[0], fields[1]);
        userMap.put(user.getId(), user);
    }

    public DictionaryUser getUser(int id) {
        return userMap.get(id);
    }

    private void addComputer(String[] fields) {
        DictionaryComputer computer = new DictionaryComputer(Integer.valueOf(fields[1]), fields[0]);
        computerMap.put(computer.getId(), computer);
    }

    private void addApplication(String[] fields) {
        DictionaryApplication application = new DictionaryApplication(Integer.valueOf(fields[1]), fields[0]);
        applicationMap.put(application.getId(), application);
    }

    private void addEvent(String[] fields) {
        DictionaryEvent event = new DictionaryEvent(Integer.valueOf(fields[1]), fields[0]);
        eventMap.put(event.getId(), event);
    }

    private void addMetadata(String[] fields) {
        DictionaryMetadata metadata = new DictionaryMetadata(Integer.valueOf(fields[2]), fields[0], fields[1]);
        metadataMap.put(metadata.getId(), metadata);
    }

    private void addServer(String[] fields) {
        DictionaryServer server = new DictionaryServer(Integer.valueOf(fields[1]), fields[0]);
        serverMap.put(server.getId(), server);
    }

    private void addMainPort(String[] fields) {
        DictionaryMainPort port = new DictionaryMainPort(Integer.valueOf(fields[1]), fields[0]);
        mainPortMap.put(port.getId(), port);
    }

    private void addAuxiliaryPort(String[] fields) {
        DictionaryAuxiliaryPort port = new DictionaryAuxiliaryPort(Integer.valueOf(fields[1]), fields[0]);
        auxiliaryPortMap.put(port.getId(), port);
    }

}
