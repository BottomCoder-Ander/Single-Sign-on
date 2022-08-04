package cool.cade.test.gateway.perms.service.Impl;

import cool.cade.test.gateway.perms.dao.MenusDaoRepository;
import cool.cade.test.gateway.perms.entity.MenuEntity;
import cool.cade.test.gateway.perms.service.MenuService;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collector;

/**
 * @author :Ander
 * @date : 2022/8/2
 */
@Service
public class MenuServiceImpl implements MenuService {

    private List<Pair<String, String>> perms;

    private Lock findPermsLock = new ReentrantLock();
    private MenusDaoRepository menusDaoRepository;

    public MenuServiceImpl(MenusDaoRepository menusDaoRepository) {
        this.menusDaoRepository = menusDaoRepository;
    }

    @Override
    public Mono<List<Pair<String, String>>> findAllMenusToPair(){
        return findAllMenusToPair(false);
    }
    @Override
    public Mono<List<Pair<String, String>>> findAllMenusToPair(boolean force){
        if(force || perms == null) {
            findPermsLock.lock();
            if(perms != null) return Mono.just(perms);

            return menusDaoRepository.findAll().collect(
                Collector.<MenuEntity,List<Pair<String, String>>, List<Pair<String, String>>>of(
                    ArrayList::new,
                    (list, ele) -> list.add(ImmutablePair.of(ele.getPath(), ele.getPerms())),
                    (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    },
                    (result) -> {
                        perms = result;
                        return result;
                    }
            )).doFinally(signalType->{
                findPermsLock.unlock();
                // 正常情况下SignalType是Cancel
                if(!signalType.equals(SignalType.ON_ERROR)) {
                    Assert.notNull(perms, "perms must not be null !");
                }
            });
        }
        return Mono.just(perms);
    }
}
