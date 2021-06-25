jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PilotoService } from '../service/piloto.service';
import { IPiloto, Piloto } from '../piloto.model';

import { PilotoUpdateComponent } from './piloto-update.component';

describe('Component Tests', () => {
  describe('Piloto Management Update Component', () => {
    let comp: PilotoUpdateComponent;
    let fixture: ComponentFixture<PilotoUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pilotoService: PilotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PilotoUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PilotoUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PilotoUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pilotoService = TestBed.inject(PilotoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const piloto: IPiloto = { id: 456 };

        activatedRoute.data = of({ piloto });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(piloto));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const piloto = { id: 123 };
        spyOn(pilotoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ piloto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: piloto }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pilotoService.update).toHaveBeenCalledWith(piloto);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const piloto = new Piloto();
        spyOn(pilotoService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ piloto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: piloto }));
        saveSubject.complete();

        // THEN
        expect(pilotoService.create).toHaveBeenCalledWith(piloto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const piloto = { id: 123 };
        spyOn(pilotoService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ piloto });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pilotoService.update).toHaveBeenCalledWith(piloto);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
