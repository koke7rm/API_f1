import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPiloto, Piloto } from '../piloto.model';
import { PilotoService } from '../service/piloto.service';

@Component({
  selector: 'jhi-piloto-update',
  templateUrl: './piloto-update.component.html',
})
export class PilotoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellido: [null, [Validators.pattern('^[a-zA-Z ]*$')]],
    edad: [],
    coche: [],
  });

  constructor(protected pilotoService: PilotoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ piloto }) => {
      this.updateForm(piloto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const piloto = this.createFromForm();
    if (piloto.id !== undefined) {
      this.subscribeToSaveResponse(this.pilotoService.update(piloto));
    } else {
      this.subscribeToSaveResponse(this.pilotoService.create(piloto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPiloto>>): void {
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

  protected updateForm(piloto: IPiloto): void {
    this.editForm.patchValue({
      id: piloto.id,
      nombre: piloto.nombre,
      apellido: piloto.apellido,
      edad: piloto.edad,
      coche: piloto.coche,
    });
  }

  protected createFromForm(): IPiloto {
    return {
      ...new Piloto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      edad: this.editForm.get(['edad'])!.value,
      coche: this.editForm.get(['coche'])!.value,
    };
  }
}
