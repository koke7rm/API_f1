import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPiloto, Piloto } from '../piloto.model';

import { PilotoService } from './piloto.service';

describe('Service Tests', () => {
  describe('Piloto Service', () => {
    let service: PilotoService;
    let httpMock: HttpTestingController;
    let elemDefault: IPiloto;
    let expectedResult: IPiloto | IPiloto[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PilotoService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        apellido: 'AAAAAAA',
        edad: 0,
        coche: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Piloto', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Piloto()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Piloto', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            apellido: 'BBBBBB',
            edad: 1,
            coche: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Piloto', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            apellido: 'BBBBBB',
          },
          new Piloto()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Piloto', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            apellido: 'BBBBBB',
            edad: 1,
            coche: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Piloto', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPilotoToCollectionIfMissing', () => {
        it('should add a Piloto to an empty array', () => {
          const piloto: IPiloto = { id: 123 };
          expectedResult = service.addPilotoToCollectionIfMissing([], piloto);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(piloto);
        });

        it('should not add a Piloto to an array that contains it', () => {
          const piloto: IPiloto = { id: 123 };
          const pilotoCollection: IPiloto[] = [
            {
              ...piloto,
            },
            { id: 456 },
          ];
          expectedResult = service.addPilotoToCollectionIfMissing(pilotoCollection, piloto);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Piloto to an array that doesn't contain it", () => {
          const piloto: IPiloto = { id: 123 };
          const pilotoCollection: IPiloto[] = [{ id: 456 }];
          expectedResult = service.addPilotoToCollectionIfMissing(pilotoCollection, piloto);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(piloto);
        });

        it('should add only unique Piloto to an array', () => {
          const pilotoArray: IPiloto[] = [{ id: 123 }, { id: 456 }, { id: 38338 }];
          const pilotoCollection: IPiloto[] = [{ id: 123 }];
          expectedResult = service.addPilotoToCollectionIfMissing(pilotoCollection, ...pilotoArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const piloto: IPiloto = { id: 123 };
          const piloto2: IPiloto = { id: 456 };
          expectedResult = service.addPilotoToCollectionIfMissing([], piloto, piloto2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(piloto);
          expect(expectedResult).toContain(piloto2);
        });

        it('should accept null and undefined values', () => {
          const piloto: IPiloto = { id: 123 };
          expectedResult = service.addPilotoToCollectionIfMissing([], null, piloto, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(piloto);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
