package net.ringo.ringoSwap.service;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
 
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.auth.EvernoteSession;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.edam.type.Notebook;
import net.ringo.ringoSwap.dao.Mapper;

 
@Service
public class EverServiceImple implements EverService {
    private final EvernoteSession evernoteSession;
 
    @Override
    public Map<String, Object> service(HttpServletRequest request, Mapper mapper){
        String developerToken = "여러분의 토큰";
        EvernoteAuth evernoteAuth = null;
        ClientFactory factory = null;
        NoteStoreClient noteStore = null;
        List<Notebook> notebooks = null;
                
        try {
            evernoteAuth = new EvernoteAuth(EvernoteService.SANDBOX, developerToken);
            factory = new ClientFactory(evernoteAuth);
            noteStore = factory.createNoteStoreClient();
            notebooks = noteStore.listNotebooks();
            
        } catch (Exception e) {
            // TODO: handle exception
        }
 
        for (Notebook notebook : notebooks) {
          System.out.println("Notebook: " + notebook.getName());
        }
        
        return null;
    }
    



        public EvernoteService(EvernoteSession evernoteSession) {
            this.evernoteSession = evernoteSession;
        }

        public void performEvernoteApiCall() {HttpURLConnection;
            // 여기에서 Evernote API 메서드 호출을 수행합니다.
        	Session session = new EvernoteSession.Builder(context)
            .setEvernoteService(EvernoteService.SANDBOX)
            .setApiKey("YOUR_API_KEY")
            .setApiSecret("YOUR_API_SECRET")
            .build();
        }
    }
}
 