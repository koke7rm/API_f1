import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'piloto',
        data: { pageTitle: 'Pilotos' },
        loadChildren: () => import('./piloto/piloto.module').then(m => m.PilotoModule),
      },
      {
        path: 'carrera',
        data: { pageTitle: 'Carreras' },
        loadChildren: () => import('./carrera/carrera.module').then(m => m.CarreraModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
