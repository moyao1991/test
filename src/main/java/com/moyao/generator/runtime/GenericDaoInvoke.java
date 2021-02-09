package com.moyao.generator.runtime;

import java.time.LocalDateTime;
import java.util.List;

public class GenericDaoInvoke {

    private boolean supportVersion;

    private boolean supportDeleted;

    public GenericDaoInvoke(Class type) {
        this.supportDeleted = !NotSupportDelete.class.isAssignableFrom(type);
        this.supportVersion = !NotSupportVersion.class.isAssignableFrom(type);
    }

    public int insert(BaseDo record) {
        autoSaveBaseDo(record);
        return 0;
    }

    public int insertList(List<BaseDo> records) {
        records.forEach(this::autoSaveBaseDo);
        return 0;
    }

    public int update(BaseDo record) {
        modifyBaseDo(record);
        return 0;
    }

    public int updateByVersion(BaseDo record) {
        checkSupportVersion();
        modifyBaseDo(record);
        return 0;
    }


    public int deleteByVersion(BaseDo record) {
        checkSupportVersion();
        record.setDeleted(true);
        return 0;
    }

    private void autoSaveBaseDo(BaseDo record) {
        if (record.getId() == null) {
            record.setId(1L);
        }
        record.setVersion(0L);
        LocalDateTime now = LocalDateTime.now();
        record.setDxCreated(now);
        record.setDxModified(now);
    }

    private void modifyBaseDo(BaseDo record) {
        LocalDateTime now = LocalDateTime.now();
        record.setDxModified(now);
    }

    private void checkSupportVersion() {
        if (!supportVersion) {
            throw NotSupportVersion.exception();
        }
    }
}
