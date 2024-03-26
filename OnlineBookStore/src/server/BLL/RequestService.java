package server.BLL;


import server.DAL.RequestDAL;
import server.model.Request;
import java.sql.SQLException;
import java.util.List;

public class RequestService {
    public List<Request> getRequestsForBorrower(int borrowerId){
        requestDB.getRequestsForBorrower(borrowerId);
        return requestDB.getRequestsForBorrower(borrowerId);
    }
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
        Request request = getRequest(requestId);
        requestDB.acceptRequest(requestId);
    }
    public void rejectRequest(int requestId)
    {
        Request request = getRequest(requestId);
        requestDB.rejectRequest(requestId);
    }
    public Request getRequest(int requestId){
        Request request=null;
        request = requestDB.getRequest(requestId);
        if(request == null){
            throw new NullPointerException("Request is not found");
        }
        return request;
    }


}
