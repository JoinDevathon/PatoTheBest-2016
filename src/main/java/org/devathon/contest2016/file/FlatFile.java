package org.devathon.contest2016.file;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.devathon.contest2016.util.Constants;

import java.io.*;
import java.nio.charset.Charset;

class FlatFile extends YamlConfiguration {

    private final File file;

    FlatFile(String fileName) {
        if(!Constants.PLUGIN_DIR.exists()) {
            //noinspection ResultOfMethodCallIgnored
            Constants.PLUGIN_DIR.mkdirs();
        }

        this.file = new File(Constants.PLUGIN_DIR, fileName + ".yml");
    }

    void load() {
        if(!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("# a file");
                writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder config = new StringBuilder("");
            String line;
            int comment = 0;
            while((line = bufferedReader.readLine()) != null) {
                if(line.startsWith("#")) {
                    config.append(line.replaceFirst("#", "COMMENT_" + comment++ + ": '")).append("'\n");
                } else {
                    config.append(line).append("\n");
                }
            }

            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            loadFromString(config.toString());
        } catch(IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(File file) throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
        writer.write(generateSaveToString());
        writer.close();
    }

    private String generateSaveToString() {
        String[] config = saveToString().split("\n");
        StringBuilder configFinal = new StringBuilder("");
        for (String s : config) {
            if(s.startsWith("COMMENT_")) {
                String s2 = s.split(":")[1];
                s2 = s2.substring(1, s2.length()).replace("'", "");
                configFinal.append("#").append(s2).append("\n");
            } else {
                configFinal.append(s).append("\n");
            }
        }

        return configFinal.toString();
    }
}
