package com.greatminds.ayni.management.interfaces.rest;

import com.greatminds.ayni.management.domain.model.queries.GetAllCropsByProductIdQuery;
import com.greatminds.ayni.management.domain.model.queries.GetCropByProductIdQuery;
import com.greatminds.ayni.management.domain.services.CropQueryService;
import com.greatminds.ayni.management.interfaces.rest.resources.CropResource;
import com.greatminds.ayni.management.interfaces.rest.transform.CropResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/products/{productId}/crops", produces= MediaType.APPLICATION_JSON_VALUE)
@Tag(name="Products")
public class ProductCropsController {

    private final CropQueryService cropQueryService;

    public ProductCropsController(CropQueryService cropQueryService) {
        this.cropQueryService = cropQueryService;
    }

    @GetMapping
    public ResponseEntity<List<CropResource>> getAllCropsByProductId(@PathVariable Long productId) {
        var getAllCropsByProductIdQuery = new GetAllCropsByProductIdQuery(productId);
        var crops = cropQueryService.handle(getAllCropsByProductIdQuery);
        var cropsResource= crops.stream().map(CropResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(cropsResource);
    }

    @GetMapping("/{cropId}")
    public ResponseEntity<CropResource> getCropByProductIdAndCropId(@PathVariable Long productId, @PathVariable Long cropId) {
        var getCropByProductId = new GetCropByProductIdQuery(productId, cropId);
        var crop = cropQueryService.handle(getCropByProductId);

        if(crop.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        var cropResource = CropResourceFromEntityAssembler.toResourceFromEntity(crop.get());
        return ResponseEntity.ok(cropResource);
    }
}
