package com.realaicy.pg.showcase.upload.repository;

import com.realaicy.pg.core.repository.BaseRepository;
import com.realaicy.pg.showcase.upload.entity.Upload;

/**
 * SD-JPA-Repository：上传
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface UploadRepository extends BaseRepository<Upload, Long> {

    Upload findByName(String name);

}
