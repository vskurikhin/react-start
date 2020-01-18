package su.svn.db.integration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import su.svn.db.integration.domain.Message;

import java.util.UUID;

@Repository
public interface MessageDao extends JpaRepository<Message, UUID> {
}
