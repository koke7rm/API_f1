jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CarreraService } from '../service/carrera.service';
import { ICarrera, Carrera } from '../carrera.model';
import { IPiloto } from 'app/entities/piloto/piloto.model';
import { PilotoService } from 'app/entities/piloto/service/piloto.service';

import { CarreraUpdateComponent } from './carrera-update.component';

describe('Component Tests', () => {
  describe('Carrera Management Update Component', () => {
    let comp: CarreraUpdateComponent;
    let fixture: ComponentFixture<CarreraUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let carreraService: CarreraService;
    let pilotoService: PilotoService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CarreraUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CarreraUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CarreraUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      carreraService = TestBed.inject(CarreraService);
      pilotoService = TestBed.inject(PilotoService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Piloto query and add missing value', () => {
        const carrera: ICarrera = { id: 456 };
        const ganador: IPiloto = { id: 32642 };
        carrera.ganador = ganador;
        const segundoPuesto: IPiloto = { id: 46500 };
        carrera.segundoPuesto = segundoPuesto;
        const tercerPuesto: IPiloto = { id: 22519 };
        carrera.tercerPuesto = tercerPuesto;
        const noTerminans: IPiloto[] = [{ id: 78679 }];
        carrera.noTerminans = noTerminans;

        const pilotoCollection: IPiloto[] = [{ id: 69724 }];
        spyOn(pilotoService, 'query').and.returnValue(of(new HttpResponse({ body: pilotoCollection })));
        const additionalPilotos = [ganador, segundoPuesto, tercerPuesto, ...noTerminans];
        const expectedCollection: IPiloto[] = [...additionalPilotos, ...pilotoCollection];
        spyOn(pilotoService, 'addPilotoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ carrera });
        comp.ngOnInit();

        expect(pilotoService.query).toHaveBeenCalled();
        expect(pilotoService.addPilotoToCollectionIfMissing).toHaveBeenCalledWith(pilotoCollection, ...additionalPilotos);
        expect(comp.pilotosSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const carrera: ICarrera = { id: 456 };
        const ganador: IPiloto = { id: 97618 };
        carrera.ganador = ganador;
        const segundoPuesto: IPiloto = { id: 83025 };
        carrera.segundoPuesto = segundoPuesto;
        const tercerPuesto: IPiloto = { id: 61637 };
        carrera.tercerPuesto = tercerPuesto;
        const noTerminans: IPiloto = { id: 80519 };
        carrera.noTerminans = [noTerminans];

        activatedRoute.data = of({ carrera });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(carrera));
        expect(comp.pilotosSharedCollection).toContain(ganador);
        expect(comp.pilotosSharedCollection).toContain(segundoPuesto);
        expect(comp.pilotosSharedCollection).toContain(tercerPuesto);
        expect(comp.pilotosSharedCollection).toContain(noTerminans);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carrera = { id: 123 };
        spyOn(carreraService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carrera });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: carrera }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(carreraService.update).toHaveBeenCalledWith(carrera);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carrera = new Carrera();
        spyOn(carreraService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carrera });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: carrera }));
        saveSubject.complete();

        // THEN
        expect(carreraService.create).toHaveBeenCalledWith(carrera);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const carrera = { id: 123 };
        spyOn(carreraService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ carrera });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(carreraService.update).toHaveBeenCalledWith(carrera);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPilotoById', () => {
        it('Should return tracked Piloto primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPilotoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPiloto', () => {
        it('Should return option if no Piloto is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPiloto(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Piloto for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPiloto(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Piloto is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPiloto(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
