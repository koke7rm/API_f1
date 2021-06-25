import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CarreraService } from '../service/carrera.service';

import { CarreraComponent } from './carrera.component';

describe('Component Tests', () => {
  describe('Carrera Management Component', () => {
    let comp: CarreraComponent;
    let fixture: ComponentFixture<CarreraComponent>;
    let service: CarreraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CarreraComponent],
      })
        .overrideTemplate(CarreraComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CarreraComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CarreraService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.carreras?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
