package intj.ghchecker3.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import intj.ghchecker3.domain.ClientGHSugar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DynamoDBclientDataExtractor {

    private static Logger logger = LoggerFactory.getLogger("DynamoDBclientDataExtractor");

    public List<ClientGHSugar> getClientSugarData() {

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

        //ScanSpec scanSpec = new ScanSpec().withMaxResultSize(10);
        ScanSpec scanSpec = new ScanSpec();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable("gh.hot.sugar.v2.2018-04");
        ItemCollection<ScanOutcome> items = table.scan(scanSpec);

        List<ClientGHSugar> scanResults = new ArrayList<>();

        Iterator<Item> iter = items.iterator();
        while (iter.hasNext()) {
            Item item = iter.next();
            Map<String, Object> itemMap = item.asMap();

            String webSiteStr = (String) itemMap.get("website");
            String orgNumberStr = (String) itemMap.get("organizationNumber");
            String nameStr = (String) itemMap.get("name");

            String clientId_userIdStr = (String) itemMap.get("clientId_assignedUserId");

            String[] split = clientId_userIdStr.split("_");

            String clientIdStr = "";
            String userIdStr = "";
            if ((split.length) == 2) {
                clientIdStr = split[0];
                userIdStr = split[1];
            } else {
                logger.info(" ERROR parsing clientId/userId from Dynamo table!");
            }

            ClientGHSugar clientGHSugar = new ClientGHSugar(
                    clientIdStr, nameStr, orgNumberStr, webSiteStr, userIdStr);

            logger.info("extracted from GrowthHouse DynamoDB: " + clientGHSugar.toString());

            scanResults.add(clientGHSugar);
        }
        return scanResults;
    }
}



