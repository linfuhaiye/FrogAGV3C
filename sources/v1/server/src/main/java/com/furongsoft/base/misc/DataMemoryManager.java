package com.furongsoft.base.misc;

import com.furongsoft.base.rbac.entities.Config;
import com.furongsoft.base.rbac.entities.Dictionary;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class DataMemoryManager extends SingletonFactory {

    private static Config config;
    private static AtomicLong teamNumber;
    private static List<Dictionary> dictionaryList;

    public static DataMemoryManager getInstance() {
        return SingletonFactory.getInstance(DataMemoryManager.class);
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        DataMemoryManager.config = config;
    }

    public AtomicLong getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(AtomicLong teamNumber) {
        DataMemoryManager.teamNumber = teamNumber;
    }

    public List<Dictionary> getDictionaryList() {
        return dictionaryList;
    }

    public void setDictionaryList(List<Dictionary> dictionaryList) {
        DataMemoryManager.dictionaryList = dictionaryList;
    }

}
