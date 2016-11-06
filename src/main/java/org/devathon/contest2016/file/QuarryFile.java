package org.devathon.contest2016.file;

import org.devathon.contest2016.quarry.Quarry;

public class QuarryFile extends FlatFile {

    private final Quarry quarry;

    public QuarryFile(Quarry quarry) {
        super("quarries/" + quarry.getUuid().toString());
        this.quarry = quarry;
        load();
    }
}
