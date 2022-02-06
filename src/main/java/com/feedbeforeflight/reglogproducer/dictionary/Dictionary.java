package com.feedbeforeflight.reglogproducer.dictionary;

import com.feedbeforeflight.reglogproducer.batch.DictionaryItem;

import java.util.HashMap;

public class Dictionary {

    private HashMap<Integer, DictionaryUser> userMap;
    private HashMap<Integer, DictionaryComputer> computerMap;
    private HashMap<Integer, DictionaryApplication> applicationMap;
    private HashMap<Integer, DictionaryEvent> eventMap;
    private HashMap<Integer, DictionaryMetadata> metadataMap;
    private HashMap<Integer, DictionaryServer> serverMap;
    private HashMap<Integer, DictionaryMainPort> mainPortMap;
    private HashMap<Integer, DictionaryAuxiliaryPort> auxiliaryPortMap;

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
