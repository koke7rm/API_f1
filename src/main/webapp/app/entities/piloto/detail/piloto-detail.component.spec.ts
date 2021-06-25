import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PilotoDetailComponent } from './piloto-detail.component';

describe('Component Tests', () => {
  describe('Piloto Management Detail Component', () => {
    let comp: PilotoDetailComponent;
    let fixture: ComponentFixture<PilotoDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PilotoDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ piloto: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PilotoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PilotoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load piloto on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.piloto).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
