package server.BLL;


import server.DAL.RequestDAL;
import server.model.Request;
import java.sql.SQLException;
import java.util.List;

public class RequestService {
    private RequestDAL requestDB = new RequestDAL();

    public void addRequest(Request request) throws SQLException {
        requestDB.addRequest(request);
    }
    public List<Request> getPendingRequestsForLender(int lenderId) {
        return requestDB.getPendingRequestsForLender(lenderId);
    }
    public List<Request> getAcceptedRequestsForRequester(int requesterId) {
        return requestDB.getAcceptedRequestsForRequester(requesterId);
    }
    public List<Request> getAllRequestsForUser(int lenderId)
    {
        return requestDB.getAllRequestsForUser(lenderId);
    }
    public void acceptRequest(int requestId)
    {
        requestDB.acceptRequest(requestId);
    }
    public void rejectRequest(int requestId)
    {
        requestDB.rejectRequest(requestId);
    }


}
