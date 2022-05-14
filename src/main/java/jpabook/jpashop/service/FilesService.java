package jpabook.jpashop.service;

import jpabook.jpashop.domain.Files;
import jpabook.jpashop.repository.FilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilesService {
    @Autowired
    FilesRepository filesRepository;

    public void save(Files files) {
        Files f = new Files();
        f.setFilename(files.getFilename());
        f.setFileOriName(files.getFileOriName());
        f.setFileurl(files.getFileurl());

        filesRepository.save(f);
    }
}