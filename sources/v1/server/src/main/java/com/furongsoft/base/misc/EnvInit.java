package com.furongsoft.base.misc;

import com.furongsoft.base.rbac.mappers.ConfigDao;
import com.furongsoft.base.rbac.mappers.DictionaryDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EnvInit implements CommandLineRunner {

    private final ConfigDao configDao;
    private final DictionaryDao dictionaryDao;

    public EnvInit(ConfigDao configDao, DictionaryDao dictionaryDao) {
        this.configDao = configDao;
        this.dictionaryDao = dictionaryDao;
    }

    @Override
    public void run(String... args) throws Exception {
        DataMemoryManager.getInstance().setConfig(configDao.selectConfigById(1));
        initDictList();
    }

    private void initDictList() {
        List<Integer> codes = new ArrayList<>();
        codes.add(25);
        codes.add(26);
        DataMemoryManager.getInstance().setDictionaryList(dictionaryDao.selectDictByGroupCodes(codes));
    }
}

