import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICarrera, Carrera } from '../carrera.model';
import { CarreraService } from '../service/carrera.service';
import { IPiloto } from 'app/entities/piloto/piloto.model';
import { PilotoService } from 'app/entities/piloto/service/piloto.service';

@Component({
  selector: 'jhi-carrera-update',
  templateUrl: './carrera-update.component.html',
})
export class CarreraUpdateComponent implements OnInit {
  isSaving = false;

  pilotosSharedCollection: IPiloto[] = [];

  editForm = this.fb.group({
    id: [],
    nombreCircuito: [null, []],
    pais: [],
    ganador: [],
    segundoPuesto: [],
    tercerPuesto: [],
    noTerminans: [],
  });

  constructor(
    protected carreraService: CarreraService,
    protected pilotoService: PilotoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carrera }) => {
      this.updateForm(carrera);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const carrera = this.createFromForm();
    if (carrera.id !== undefined) {
      this.subscribeToSaveResponse(this.carreraService.update(carrera));
    } else {
      this.subscribeToSaveResponse(this.carreraService.create(carrera));
    }
  }

  trackPilotoById(index: number, item: IPiloto): number {
    return item.id!;
  }

  getSelectedPiloto(option: IPiloto, selectedVals?: IPiloto[]): IPiloto {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICarrera>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(carrera: ICarrera): void {
    this.editForm.patchValue({
      id: carrera.id,
      nombreCircuito: carrera.nombreCircuito,
      pais: carrera.pais,
      ganador: carrera.ganador,
      segundoPuesto: carrera.segundoPuesto,
      tercerPuesto: carrera.tercerPuesto,
      noTerminans: carrera.noTerminans,
    });

    this.pilotosSharedCollection = this.pilotoService.addPilotoToCollectionIfMissing(
      this.pilotosSharedCollection,
      carrera.ganador,
      carrera.segundoPuesto,
      carrera.tercerPuesto,
      ...(carrera.noTerminans ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pilotoService
      .query()
      .pipe(map((res: HttpResponse<IPiloto[]>) => res.body ?? []))
      .pipe(
        map((pilotos: IPiloto[]) =>
          this.pilotoService.addPilotoToCollectionIfMissing(
            pilotos,
            this.editForm.get('ganador')!.value,
            this.editForm.get('segundoPuesto')!.value,
            this.editForm.get('tercerPuesto')!.value,
            ...(this.editForm.get('noTerminans')!.value ?? [])
          )
        )
      )
      .subscribe((pilotos: IPiloto[]) => (this.pilotosSharedCollection = pilotos));
  }

  protected createFromForm(): ICarrera {
    return {
      ...new Carrera(),
      id: this.editForm.get(['id'])!.value,
      nombreCircuito: this.editForm.get(['nombreCircuito'])!.value,
      pais: this.editForm.get(['pais'])!.value,
      ganador: this.editForm.get(['ganador'])!.value,
      segundoPuesto: this.editForm.get(['segundoPuesto'])!.value,
      tercerPuesto: this.editForm.get(['tercerPuesto'])!.value,
      noTerminans: this.editForm.get(['noTerminans'])!.value,
    };
  }
}
