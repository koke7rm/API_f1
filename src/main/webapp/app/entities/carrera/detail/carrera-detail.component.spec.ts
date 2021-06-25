import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CarreraDetailComponent } from './carrera-detail.component';

describe('Component Tests', () => {
  describe('Carrera Management Detail Component', () => {
    let comp: CarreraDetailComponent;
    let fixture: ComponentFixture<CarreraDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CarreraDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ carrera: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CarreraDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CarreraDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load carrera on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.carrera).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
