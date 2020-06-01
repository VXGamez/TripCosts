package com.vx.dyvide.model.DB;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class SavedConfig {

    @Id(assignable = true) public long id;
    public String coverPath;
    public String playlist;


}
