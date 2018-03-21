package intj.ghchecker;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class HostNameService {

    public List<String> hostNames() throws Exception {

        File file = new File("/home/adrian/Development/SDA/Spring/gh-checker2/src/main/resources/static/hosts-to-check");

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();

        List<String> localResult = new ArrayList<>();

        while (line != null) {
            line = bufferedReader.readLine();
            if (line != null) {
                localResult.add(line.trim());
            }
        }

        return localResult;
    }
}
