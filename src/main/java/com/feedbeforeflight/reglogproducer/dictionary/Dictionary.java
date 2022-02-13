package com.feedbeforeflight.reglogproducer.dictionary;

import com.feedbeforeflight.reglogproducer.batch.DictionaryItem;
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
        return "Users: " + userMap.size() + System.lineSeparator() +
                "Computers: " + computerMap.size() + System.lineSeparator() +
                "Applications: " + applicationMap.size() + System.lineSeparator() +
                "Events: " + eventMap.size() + System.lineSeparator() +
                "Metadata items: " + metadataMap.size() + System.lineSeparator() +
                "Servers: " + serverMap.size() + System.lineSeparator() +
                "Main ports: " + mainPortMap.size() + System.lineSeparator() +
                "Auxiliary ports: " + auxiliaryPortMap.size() + System.lineSeparator();
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
        DictionaryUser user = new DictionaryUser(Integer.parseInt(fields[2]), fields[0], fields[1]);
        userMap.put(user.getId(), user);
    }

    public DictionaryUser getUser(int id) {
        return userMap.get(id);
    }

    private void addComputer(String[] fields) {
        DictionaryComputer computer = new DictionaryComputer(Integer.parseInt(fields[1]), fields[0]);
        computerMap.put(computer.getId(), computer);
    }

    public DictionaryComputer getComputer(int id) {
        return computerMap.get(id);
    }

    private void addApplication(String[] fields) {
        DictionaryApplication application = new DictionaryApplication(Integer.parseInt(fields[1]), fields[0]);
        applicationMap.put(application.getId(), application);
    }

    public DictionaryApplication getApplication(int id) {
        return applicationMap.get(id);
    }

    private void addEvent(String[] fields) {
        DictionaryEvent event = new DictionaryEvent(Integer.parseInt(fields[1]), fields[0]);
        eventMap.put(event.getId(), event);
    }

    public DictionaryEvent getEvent(int id) {
        return eventMap.get(id);
    }

    private void addMetadata(String[] fields) {
        DictionaryMetadata metadata = new DictionaryMetadata(Integer.parseInt(fields[2]), fields[0], fields[1]);
        metadataMap.put(metadata.getId(), metadata);
    }

    public DictionaryMetadata getMetadata(int id) {
        return metadataMap.get(id);
    }

    private void addServer(String[] fields) {
        DictionaryServer server = new DictionaryServer(Integer.parseInt(fields[1]), fields[0]);
        serverMap.put(server.getId(), server);
    }

    public DictionaryServer getServer(int id) {
        return serverMap.get(id);
    }

    private void addMainPort(String[] fields) {
        DictionaryMainPort port = new DictionaryMainPort(Integer.parseInt(fields[1]), Integer.parseInt(fields[0]));
        mainPortMap.put(port.getId(), port);
    }

    public DictionaryMainPort getMainPort(int id) {
        return mainPortMap.get(id);
    }

    private void addAuxiliaryPort(String[] fields) {
        DictionaryAuxiliaryPort port = new DictionaryAuxiliaryPort(Integer.parseInt(fields[1]), Integer.parseInt(fields[0]));
        auxiliaryPortMap.put(port.getId(), port);
    }

    public DictionaryAuxiliaryPort getAuxiliaryPort(int id) {
        return auxiliaryPortMap.get(id);
    }
}
