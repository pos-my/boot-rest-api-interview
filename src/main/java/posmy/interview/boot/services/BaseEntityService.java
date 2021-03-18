package posmy.interview.boot.services;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import posmy.interview.boot.entity.IEntity;

import javax.persistence.EntityNotFoundException;
import java.io.Serializable;

public abstract class BaseEntityService<E extends IEntity<ID>, ID extends Serializable> extends BaseService {

    protected abstract PagingAndSortingRepository<E, ID> getRepository();

    @Transactional
    public E create(E entity) {
        doCreate(entity);
        return getRepository().save(entity);
    }

    @Transactional
    public E update(E changeSet) {
        E entity = this.getById(changeSet.getId());
        doUpdate(entity, changeSet);
        return getRepository().save(entity);
    }

    @Transactional
    public void delete(E entity) {
        assert entity != null;
        final E pEntity = getById(entity.getId());
        getRepository().delete(pEntity);
    }

    protected abstract void doCreate(E entity);

    protected abstract E doUpdate(E entity, E changeSet);

    protected abstract void doDelete(E entity);

    @Transactional(readOnly = true)
    public E getById(ID id) {
        // todo: dedicated method
        return this
                .getRepository()
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("%s not found", id)));
    }

    protected boolean isChanged(Object persistentObj, Object incoming) {
        if (persistentObj == null && incoming == null) {
            return false;
        }
        if (persistentObj == null && incoming != null) {
            return true;
        }
        // If incoming change is NULL, assume no change!
        if (persistentObj != null && incoming == null) {
            return false;
        }

        if (persistentObj
                .getClass()
                .equals(incoming.getClass())) {
            return !persistentObj.equals(incoming);
        }
        // sound reasonable for now
        return false;
    }
}
