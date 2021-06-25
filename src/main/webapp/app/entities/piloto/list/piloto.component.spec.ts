import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PilotoService } from '../service/piloto.service';

import { PilotoComponent } from './piloto.component';

describe('Component Tests', () => {
  describe('Piloto Management Component', () => {
    let comp: PilotoComponent;
    let fixture: ComponentFixture<PilotoComponent>;
    let service: PilotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PilotoComponent],
      })
        .overrideTemplate(PilotoComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PilotoComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PilotoService);

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
      expect(comp.pilotos?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
