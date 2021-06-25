import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICarrera } from '../carrera.model';

@Component({
  selector: 'jhi-carrera-detail',
  templateUrl: './carrera-detail.component.html',
})
export class CarreraDetailComponent implements OnInit {
  carrera: ICarrera | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ carrera }) => {
      this.carrera = carrera;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
