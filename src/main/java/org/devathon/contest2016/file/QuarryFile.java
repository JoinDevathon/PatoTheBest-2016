package org.devathon.contest2016.file;

import org.devathon.contest2016.quarry.Quarry;

public class QuarryFile extends FlatFile {

    public QuarryFile(Quarry quarry) {
        super("quarries/" + quarry.getUuid().toString());
        load();
    }
}
