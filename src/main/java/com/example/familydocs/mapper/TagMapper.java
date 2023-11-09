package com.example.familydocs.mapper;


import com.example.familydocs.dto.TagDTO;
import com.example.familydocs.model.Tag;
import com.example.familydocs.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TagMapper {

    private final TagRepository tagRepository;

    @Autowired
    public TagMapper(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public TagDTO toDTO(Tag tag) {

        if (tag == null) { return null; }

        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setTagName(tag.getTagName());

        return dto;
    }

    public Tag toEntity(TagDTO dto) {
        if (dto == null) { return null; }

        return tagRepository.findByTagName(dto.getTagName())
                .orElseGet(() -> {
                    Tag tag = new Tag();
                    tag.setTagName(dto.getTagName());
                    return tag;
                });
    }
}
