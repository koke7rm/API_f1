import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPiloto } from '../piloto.model';

@Component({
  selector: 'jhi-piloto-detail',
  templateUrl: './piloto-detail.component.html',
})
export class PilotoDetailComponent implements OnInit {
  piloto: IPiloto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ piloto }) => {
      this.piloto = piloto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
