package su.svn.db.integration.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import su.svn.db.integration.dao.MessageDao;
import su.svn.db.integration.domain.Message;

import java.util.List;
import java.util.UUID;

@Repository
@Transactional(transactionManager = "transactionManagerIntegration")
public class MessageRepository {

    private final MessageDao messageDao;

    public MessageRepository(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public List<Message> findAll() {
        return messageDao.findAll();
    }

    public List<Message> save(Message message) {
        messageDao.save(message);
        return findAll();
    }

    public List<Message> deleteById(UUID id) {
        messageDao.deleteById(id);
        return findAll();
    }
}
