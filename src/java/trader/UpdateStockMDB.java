package trader;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 *
 * @author jose
 */
@MessageDriven(mappedName = "jms/UpdateStock", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class UpdateStockMDB implements MessageListener {
    
    @EJB private BrokerModel brokerModel;
    
    public UpdateStockMDB(){
    }
    
    @Override
    public void onMessage(Message message) {
        
        try {
            
            TextMessage tMsg = (TextMessage) message;
            String text = tMsg.getText();
            System.out.println("Received Message: " + text);
            String[] strings = text.split(",");
            String symbol = strings[0];
            double price = Double.parseDouble(strings[1]);
            Stock stock = brokerModel.getStock(symbol);
            stock.setPrice(price);
            brokerModel.updateStock(stock);
            
        }catch(Exception e){
            
            System.out.println("MDB Exception " + e.getMessage());
            
        }
    }
}
